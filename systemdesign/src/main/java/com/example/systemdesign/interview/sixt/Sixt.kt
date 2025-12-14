package com.example.systemdesign.interview.sixt

/**
A user selects pickup/return dates and locations and taps “See Vehicles”, which navigates to a results screen.
On this screen, the product requires showing one recommended vehicle at the top (if available, with a banner/upsell
message) followed by all other available vehicles.

The backend provides two APIs:
Recommendation API: returns at most one vehicle, or nothing if unavailable.
Vehicles API: always returns the full list of vehicles.

The recommendation may or may not exist depending on availability for the selected date and location.
Design and explain how you would fetch, combine, and expose this data to the UI using Clean Architecture and MVVM.
Focus on the data layer and ViewModel, including how network models are mapped and handled.

pickup date, return date, location
btn -> see all vehicle

2 endpoints:
-- /recommendation --> only 1 vehicle if exist else nothing
-- /vehicles --> will always return list of vehicles
 */

/**
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.systemdesign.BuildConfig
import com.example.systemdesign.interview.sixt.mock.JsonAssetReader
import com.example.systemdesign.interview.sixt.mock.MockVehicleApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.collections.map
import kotlin.fold
import kotlin.jvm.java
import kotlin.let
import kotlin.runCatching

// ---------------- Data ----------------

interface ApiService {
    @GET("recommendation")
    suspend fun getRecommendation(
        /*  @Query("pickupDate") pickupDate: String,
          @Query("returnDate") returnDate: String,
          @Query("location") returnLocation: String,*/
    ): Response<RecommendationDto>

    @GET("vehicles")
    suspend fun getVehicles(
        /*  @Query("pickupDate") pickupDate: String,
           @Query("returnDate") returnDate: String,
           @Query("location") returnLocation: String,*/
    ): VehicleListDto
}

data class VehicleDto(
    val id: String,
    val name: String,
    val price: Double
)

data class RecommendationDto(
    val bannerText: String?,
    val vehicle: VehicleDto?
)

data class VehicleListDto(
    val vehicles: List<VehicleDto>
)

// mapper
fun VehicleDto.toDomain() = Vehicle(
    id = id,
    name = name,
    price = price
)

fun RecommendationDto.toDomain(): Vehicle? = this.vehicle?.toDomain()

class VehicleRepositoryImpl @Inject constructor(private val api: ApiService) : VehicleRepository {
    override suspend fun getVehiclesWithRecommendation(): Result<VehiclesWithRecommendation> =
        runCatching {
            supervisorScope {
                val recommendationDeferred = async {
                    api.getRecommendation()
                        .takeIf { it.isSuccessful }
                        ?.body()
                        ?.toDomain()
                }

                val vehiclesDeferred = async {
                    api.getVehicles().vehicles.map { it.toDomain() }
                }

                VehiclesWithRecommendation(
                    recommend = recommendationDeferred.await(),
                    vehicles = vehiclesDeferred.await()
                )
            }
        }
}

// ---------------- Domain ----------------

data class Vehicle(
    val id: String,
    val name: String,
    val price: Double
)

data class VehiclesWithRecommendation(
    val recommend: Vehicle?,
    val vehicles: List<Vehicle>
)

interface VehicleRepository {
    suspend fun getVehiclesWithRecommendation(): Result<VehiclesWithRecommendation>
}

class GetVehiclesWithRecommendationUseCase @Inject constructor(val repo: VehicleRepository) {
    suspend operator fun invoke(): Result<VehiclesWithRecommendation> = repo.getVehiclesWithRecommendation()
}


// ---------------- Presentation ----------------

data class VehicleUiState(
    val isLoading: Boolean = false,
    val recommended: Vehicle? = null,
    val vehicles: List<Vehicle> = emptyList(),
    val isError: Boolean = false
)

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val getVehiclesUseCase: GetVehiclesWithRecommendationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleUiState())
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true, isError = false) }

        viewModelScope.launch {
            getVehiclesUseCase().fold(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recommended = data.recommend,
                            vehicles = data.vehicles
                        )
                    }
                },
                onFailure = {
                    _uiState.update {
                        it.copy(isLoading = false, isError = true)
                    }
                }
            )
        }
    }
}
@Composable
fun VehiclesScreen(
    viewModel: VehicleViewModel = hiltViewModel(),
    onVehicleClicked: (Vehicle) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.isError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Something went wrong")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                // ✅ Recommended section INSIDE list
                state.recommended?.let { vehicle ->
                    item {
                        Text(
                            text = "Recommended",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )

                        VehicleRow(vehicle) {
                            onVehicleClicked(vehicle)
                        }
                    }
                }

                items(state.vehicles) { vehicle ->
                    VehicleRow(vehicle) {
                        onVehicleClicked(vehicle)
                    }
                }
            }
        }
    }
}

@Composable
fun VehicleRow(
    vehicle: Vehicle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = vehicle.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "₹${vehicle.price}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                VehiclesScreen(
                    onVehicleClicked = {}
                )
            }
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockVehicleApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealVehicleApi

@Module
@InstallIn(SingletonComponent::class)
object VehicleApiModule {

    // ---------------- REAL API ----------------
    @Provides
    @Singleton
    @RealVehicleApi
    fun provideRealApiService(): ApiService =
        Retrofit.Builder()
            .baseUrl("https://api.mockfly.dev/mocks/afc70459-bddc-4d16-b6b3-1b649eec78bc/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    // ---------------- MOCK API ----------------
    @Provides
    @Singleton
    @MockVehicleApi
    fun provideMockVehicleApi(
        jsonAssetReader: JsonAssetReader
    ): ApiService = MockVehicleApiService(jsonAssetReader)

    // ---------------- FINAL API (USED EVERYWHERE) ----------------
    @Provides
    @Singleton
    fun provideVehicleApi(
        @MockVehicleApi mockApi: ApiService,
        @RealVehicleApi realApi: ApiService
    ): ApiService {
        return if (BuildConfig.USE_MOCK_API) mockApi else realApi
    }

    // ---------------- REPOSITORY ----------------
    @Provides
    @Singleton
    fun providesRepository(
        api: ApiService,
    ): VehicleRepository =
        VehicleRepositoryImpl(api)

}

@HiltAndroidApp
class VehicleApp: Application()

**/
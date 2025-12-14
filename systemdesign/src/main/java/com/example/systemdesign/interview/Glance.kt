package com.example.systemdesign.interview

/**
Share the code and design where you need to make  call Network call to get 10 images each time UI and  show image in a
form of list by its index  0 image, 1 image, 2 image.. etc
 */

/**

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.apply
import kotlin.collections.map
import kotlin.jvm.java


// ---------------- Data ----------------

interface ImageApiService {
    @GET("images")
    suspend fun fetchImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<ImageDto>
}

data class ImageDto(
    val id: String,
    val url: String
)

fun ImageDto.toDomain() = ImageItem(id, url)

class ImagePagingSource(
    private val api: ImageApiService
) : PagingSource<Int, ImageItem>() {

    override fun getRefreshKey(state: PagingState<Int, ImageItem>): Int? =
        state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageItem> {
        return try {
            val page = params.key ?: 1
            val images = api.fetchImages(
                page = page,
                limit = params.loadSize
            ).map { it.toDomain() }

            LoadResult.Page(
                data = images,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (images.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class ImageRepositoryImpl @Inject constructor(private val api: ImageApiService) : ImageRepository {

    override fun fetchImages(): Flow<PagingData<ImageItem>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImagePagingSource(api) }
        ).flow
}

// ---------------- Domain ----------------

data class ImageItem(
    val id: String,
    val url: String
)

interface ImageRepository {
    fun fetchImages(): Flow<PagingData<ImageItem>>
}

// ---------------- Presentation ----------------

@HiltViewModel
class ImageViewModel @Inject constructor(repository: ImageRepository): ViewModel() {
    val images: Flow<PagingData<ImageItem>> = repository
        .fetchImages()
        .cachedIn(viewModelScope)
}

@Composable
fun ImageListScreen(
    viewModel: ImageViewModel = hiltViewModel()
) {
    val images = viewModel.images.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = images.itemCount,
            key = { index -> images[index]?.id ?: index }
        ) { index ->
            images[index]?.let { item ->
                ImageRow(
                    index = index,
                    url = item.url
                )
            }
        }

        when {
            images.loadState.refresh is LoadState.Loading -> {
                item {
                    CenteredLoader()
                }
            }
            images.loadState.append is LoadState.Loading -> {
                item {
                    CenteredLoader()
                }
            }
            images.loadState.append is LoadState.Error -> {
                item {
                    Text("Error loading more images")
                }
            }
        }
    }
}

@Composable
fun CenteredLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ImageRow(index: Int, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "$index image",
            modifier = Modifier.width(80.dp)
        )
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(start = 8.dp)
        )
    }
}

@AndroidEntryPoint
class ImagesMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    ImageListScreen()
                }
            }
        }
    }
}

@HiltAndroidApp
class ImagesApp: Application()


// ---------------- di ----------------
@Module
@InstallIn(SingletonComponent::class)
object ImagesAppModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.mockfly.dev/mocks/afc70459-bddc-4d16-b6b3-1b649eec78bc/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesImageApi(retrofit: Retrofit): ImageApiService {
        return retrofit.create(ImageApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesImageRepository(api: ImageApiService): ImageRepository = ImageRepositoryImpl(api)
}

        **/
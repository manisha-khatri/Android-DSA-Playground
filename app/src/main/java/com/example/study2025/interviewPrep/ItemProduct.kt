package com.example.study2025.interviewPrep


import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// 1. Expanded ProductDetail Data Class
data class ProductDetail(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String, // Renamed from 'image' for clarity
    val description: String,
    val rating: Double, // e.g., 0.0 to 5.0
    val currency: String = "$",
    val isInStock: Boolean = true
)

// 2. ProductCard Composable
@Composable
fun ProductCard(
    product: ProductDetail,
    onAddToCartClick: (ProductDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Product Image
            // For a real app, you'd use Coil's AsyncImage or Glide to load images from URLs
            // Example with Coil:
            /*            AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.placeholder_image) // A local placeholder
                        )*/
            // Using a simple Image with a placeholder color for demonstration without Coil dependency
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0)), // Light grey placeholder background
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Product Image",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                // You could also use a simple image for local drawables
                // Image(
                //     painter = painterResource(id = R.drawable.your_product_image),
                //     contentDescription = product.name,
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Crop
                // )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product Description
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price
                Text(
                    text = "${product.currency}${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating Star",
                        tint = Color(0xFFFFC107), // A nice yellow for stars
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating}/5.0",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add to Cart Button
            Button(
                onClick = { onAddToCartClick(product) },
                modifier = Modifier.fillMaxWidth(),
                enabled = product.isInStock, // Disable if out of stock
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (product.isInStock) "Add to Cart" else "Out of Stock",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// Preview Composable
@Preview(showBackground = true, widthDp = 360)
@Composable
fun ProductCardPreview() {
    MaterialTheme { // Wrap in MaterialTheme for proper styling in preview
        Surface(color = MaterialTheme.colorScheme.background) {
            val sampleProduct = ProductDetail(
                id = "P001",
                name = "Organic Whole Wheat Bread",
                price = 4.99,
                imageUrl = "https://placehold.co/600x400/000000/FFFFFF?text=Product+Image",
                description = "Freshly baked organic whole wheat bread, perfect for sandwiches or toast. Made with natural ingredients.",
                rating = 4.7,
                currency = "$",
                isInStock = true
            )
            ProductCard(product = sampleProduct, onAddToCartClick = { product ->
                println("Added ${product.name} to cart!")
            })
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun OutOfStockProductCardPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val outOfStockProduct = ProductDetail(
                id = "P002",
                name = "Artisan Sourdough Loaf",
                price = 6.50,
                imageUrl = "https://placehold.co/600x400/000000/FFFFFF?text=Out+of+Stock",
                description = "Hand-crafted sourdough loaf with a crispy crust and soft, airy interior. Currently unavailable.",
                rating = 4.9,
                currency = "$",
                isInStock = false
            )
            ProductCard(product = outOfStockProduct, onAddToCartClick = { product ->
                println("Tried to add ${product.name} to cart, but it's out of stock!")
            })
        }
    }
}
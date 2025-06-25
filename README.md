# 📱 Android + DSA Practice Playground

Welcome to my personal learning project combining **Android development** and **Data Structures & Algorithms**. This repository is a hands-on journal of my journey toward mastering Android (with Jetpack Compose, MVVM, etc.) and preparing for coding interviews, especially for senior Android roles.

---

## 🧠 Project Overview

This repository contains the following major sections:

### 📦 Android Modules

| Folder | Description |
|--------|-------------|
| `activity/` | Basic Android Activity usage (e.g., `LearningActivity`) |
| `callbackinterface/` | Examples of callback interfaces & async patterns |
| `compose/` | Jetpack Compose explorations including themes, UI, and side-effects |
| `quoteapp/` | A mini Compose app showing list/detail screens using MVVM architecture |
| `tweetsapp/` | Twitter-style UI screens built with Jetpack Compose |
| `fragment/` | Fragment communication and `SharedViewModel` patterns |
| `jetpackcompose/` | Dedicated Compose activity and text notes |
| `listingscreenproj/` | Product listing screen using MVVM, Room, and Clean Architecture |
| `livedata/` & `livedata2/` | Custom LiveData implementations and examples |
| `mvvm/` | A fully structured MVVM module with API, repository, ViewModel, and UI |
| `recyclerview/` | DiffUtil, multi-view types, and ListView examples |
| `viewmodel/` | ViewModel usage examples |

---

### 🎯 Jetpack Compose Focus Areas

- Recomposition (`RememberExample`, `RecomposableExample`)
- Side Effects:
    - `LaunchedEffect`, `DisposableEffect`, `ProduceState`, `DerivedStateOf`
- Theming: Typography, Color, Style in Compose
- Custom Composables and Modifiers
- Compose with RecyclerView and XML Interop

---

### 💡 MVVM + Clean Architecture

- **Use Cases** (`GetAllProductsUseCase`)
- **Repositories** (`ProductRepository`, `PostRepository`)
- **Room Database** (`ProductDao`, `AppDatabase`)
- **UI & ViewModel** integration

---

### 📊 Data Structures & Algorithms (DSA)

Located under `datastructure/`, organized by topic for interview prep.

#### ✅ Covered Topics:
- Arrays (Kadane's Algo, Sliding Window, 2Sum/3Sum)
- Dynamic Programming (Knapsack, LIS, Edit Distance, etc.)
- Graphs (Adjacency List & Matrix)
- Heaps & Priority Queues
- Recursion & Backtracking
- Linked Lists (Reversal, Merging, Flattening)
- Trees (Binary Tree, LCA, Subtree Sum)
- Tries and HashMaps

---

### 🧪 Testing

- `unittests/`: JUnit examples like `CalculatorTest`
- `androidTest/`: `ExampleInstrumentedTest` for UI/integration

---


## 🚀 How to Run

Clone the repo and open it in Android Studio Arctic Fox or higher:

```bash
git clone https://github.com/<your-username>/<repo-name>.git

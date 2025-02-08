package com.rudraksha.documentone.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rudraksha.documentone.data.database.DocumentDatabase
import com.rudraksha.documentone.data.model.DocumentEntity
import com.rudraksha.documentone.data.repo.DocumentRepository
import com.rudraksha.documentone.ui.navigation.Screen
import com.rudraksha.documentone.ui.screens.CreateDocumentScreen
import com.rudraksha.documentone.ui.screens.DocumentViewerScreen
import com.rudraksha.documentone.ui.screens.HomeScreen
import com.rudraksha.documentone.ui.screens.SettingsScreen
import com.rudraksha.documentone.ui.screens.TextExtractionScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentApp(
    context: Context
) {
    val database = DocumentDatabase.getInstance(context)
    val documentDao = database.documentDao()
    val documentRepository = DocumentRepository(documentDao)
    val navHostController = rememberNavController()
    val scope = rememberCoroutineScope()

    /*    LaunchedEffect(documentId) {
            try {
                document = withContext(Dispatchers.IO) {
                    database.documentDao().getDocumentById(documentId)
                }
            } catch (e: Exception) {
                errorMessage = "Error loading document: ${e.localizedMessage}"
            }
        }*/

    /*
                val articleStr = Json.encodeToString(item)
                rootNavController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("article", articleStr)
                }
                rootNavController.navigate(Route.NewsDetail)


        composable<Route.NewsDetail> {
            navHostController.previousBackStackEntry?.savedStateHandle?.get<String>("article")?.let { article ->
                val currentArticle: Article = Json.decodeFromString(article)
                ArticleDetailScreen(navHostController, currentArticle)
            }
        }*/
//    HomeScreen()

    NavHost(
        navController = navHostController,
        startDestination = Screen.Home,
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onDocumentSelected = { document ->
                    val docStr = Json.encodeToString(document)
                    navHostController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("document", docStr)
                    }
                    navHostController.navigate(Screen.View)
                },
                onNavItemClick = { route ->
                    navHostController.navigate(route)
                },
            )
        }
        composable<Screen.Create> {
            CreateDocumentScreen(
                addDocument = { documentEntity ->
                    scope.launch {
                        // documentViewModel.addDocument(documentEntity)
                        // documentViewModel.fetchDocuments()
                    }
                },
                onDocumentCreated = { documentEntity ->
                    scope.launch {
//                        Toast.makeText(context, "Navigating to View", Toast.LENGTH_LONG).show()
                        val docStr = Json.encodeToString(documentEntity)
                        navHostController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("document", docStr)
                        }
                        navHostController.navigate(Screen.View)
                    }
                },
                onNavItemClick = { route ->
                    navHostController.navigate(route)
                },
            )
        }
        composable<Screen.View> {
            navHostController.currentBackStackEntry?.savedStateHandle
                ?.get<String>("document")?.let { docStr ->
                    val document = Json.decodeFromString<DocumentEntity>(docStr)
                    DocumentViewerScreen(
                        document = document,
                        onBack = { navHostController.popBackStack() }
                    )
                }
        }
        composable<Screen.Extract> {
            TextExtractionScreen(
                onNavItemClick = { route ->
                    navHostController.navigate(route)
                },
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}

/*
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DocumentApp(database: DocumentDatabase) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToDocumentList = { navController.navigate("documentList") },
                onNavigateToCreateDocument = { navController.navigate("createDocument") },
                onNavigateToTextExtraction = { navController.navigate("textExtraction") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("documentList") {
            DocumentListScreen(
                database = database,
                onDocumentSelected = { docId -> navController.navigate("documentViewer/$docId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "documentViewer/{docId}",
            arguments = listOf(navArgument("docId") { type = NavType.IntType })
        ) { backStackEntry ->
            val docId = backStackEntry.arguments?.getInt("docId") ?: -1
            DocumentViewerScreen(
                documentId = docId,
                database = database,
                onBack = { navController.popBackStack() }
            )
        }
    }

}
*/
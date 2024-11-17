package com.potentialServices.passwordmanager.utils

sealed class MainActivityEvents {
    class  SearchEvent(val key:String):MainActivityEvents()
    class  SortByTitle(val sortingOder: SortingOder):MainActivityEvents()
    class  SortByUsername(val sortingOder: SortingOder):MainActivityEvents()
    class  SortByWebsite(val sortingOder: SortingOder):MainActivityEvents()
    class NoEvent():MainActivityEvents()

}
package com.potentialServices.passwordmanager.passwordlock

object IsPinCheckedOnce {
    var isFirstTime=true;
    fun setPinCheckedOnceTrue()
    {
        isFirstTime =false;
    }
    fun getIsPinChecked():Boolean
    {
        return isFirstTime;
    }

}
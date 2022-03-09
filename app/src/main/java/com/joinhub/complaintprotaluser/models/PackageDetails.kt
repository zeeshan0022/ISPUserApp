package com.joinhub.complaintprotaluser.models

data class PackageDetails(val pkgID:Int, val pkgName:String, val pkgDesc:String, val pkgSpeed:String,
                          val pkgVolume:String, val pkgRate:Double, val pkgBouns_Speed:String, val pkgBanner:ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackageDetails

        if (pkgID != other.pkgID) return false
        if (pkgName != other.pkgName) return false
        if (pkgDesc != other.pkgDesc) return false
        if (pkgSpeed != other.pkgSpeed) return false
        if (pkgVolume != other.pkgVolume) return false
        if (pkgRate != other.pkgRate) return false
        if (pkgBouns_Speed != other.pkgBouns_Speed) return false
        if (!pkgBanner.contentEquals(other.pkgBanner)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pkgID
        result = 31 * result + pkgName.hashCode()
        result = 31 * result + pkgDesc.hashCode()
        result = 31 * result + pkgSpeed.hashCode()
        result = 31 * result + pkgVolume.hashCode()
        result = 31 * result + pkgRate.hashCode()
        result = 31 * result + pkgBouns_Speed.hashCode()
        result = 31 * result + pkgBanner.contentHashCode()
        return result
    }
}

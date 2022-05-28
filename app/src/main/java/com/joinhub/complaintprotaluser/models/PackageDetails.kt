package com.joinhub.complaintprotaluser.models

import android.os.Parcel
import android.os.Parcelable

data class PackageDetails(var pkgID:Int=0, var pkgName:String="", var pkgDesc:String="", var pkgSpeed:String="",
                          var pkgVolume:String="", var pkgRate:Double=0.0, var pkgBouns_Speed:String="",
                          var pkgBanner:ByteArray="".toByteArray()):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.createByteArray()!!
    ) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pkgID)
        parcel.writeString(pkgName)
        parcel.writeString(pkgDesc)
        parcel.writeString(pkgSpeed)
        parcel.writeString(pkgVolume)
        parcel.writeDouble(pkgRate)
        parcel.writeString(pkgBouns_Speed)
        parcel.writeByteArray(pkgBanner)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PackageDetails> {
        override fun createFromParcel(parcel: Parcel): PackageDetails {
            return PackageDetails(parcel)
        }

        override fun newArray(size: Int): Array<PackageDetails?> {
            return arrayOfNulls(size)
        }
    }
}

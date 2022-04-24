package com.example.projet_coditty_goubin.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projet_coditty_goubin.BR


@Keep
@Entity(tableName = "explication")
data class Explication (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @ColumnInfo(name = "description")
    private var _description: String? = ""
) : Parcelable,
    BaseObservable() {

    var id: Long
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    var description: String?
        @Bindable get() = _description
        set(value) {
            _description = value
            notifyPropertyChanged(BR.description)
        }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeLong(id)
        parcel.writeString(description)


    }

    companion object CREATOR : Parcelable.Creator<Explication> {
        override fun createFromParcel(parcel: Parcel): Explication {
            return Explication(parcel)
        }

        override fun newArray(size: Int): Array<Explication?> {
            return arrayOfNulls(size)
        }
    }
}
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
@Entity(tableName = "card")
data class Card (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @ColumnInfo(name = "description")
    private var _description: String? = "",

    @ColumnInfo(name = "pathImage")
    private var _pathImage: String? = "",

    @ColumnInfo(name = "yesHealth")
    private var _yesHealth: Float = 0.0F,

    @ColumnInfo(name = "noHealth")
    private var _noHealth: Float = 0.0F,

    @ColumnInfo(name = "yesTemperature")
    private var _yesTemperature: Float = 0.0F,

    @ColumnInfo(name = "noTemperature")
    private var _noTemperature: Float = 0.0F,

    @ColumnInfo(name = "yesFonte")
    private var _yesFonte: Float = 0.0F,

    @ColumnInfo(name = "noFonte")
    private var _noFonte: Float = 0.0F,

    @ColumnInfo(name = "yesDeath")
    private var _yesDeath: Int = 0,

    @ColumnInfo(name = "noDeath")
    private var _noDeath: Int = 0,

    @ColumnInfo(name = "explication")
    private var _explication: String? = ""
) : Parcelable,
    BaseObservable() {

    var id: Long
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    var description:  String?
        @Bindable get() = _description
        set(value) {
            _description = value
            notifyPropertyChanged(BR.description)
        }

    var pathImage:  String?
        @Bindable get() = _pathImage
        set(value) {
            _pathImage = value
            notifyPropertyChanged(BR.pathImage)
        }

    var yesHealth:  Float
        @Bindable get() = _yesHealth
        set(value) {
            _yesHealth = value
            notifyPropertyChanged(BR.yesHealth)
        }

    var noHealth:  Float
        @Bindable get() = _noHealth
        set(value) {
            _noHealth = value
            notifyPropertyChanged(BR.noHealth)
        }

    var yesFonte:  Float
        @Bindable get() = _yesFonte
        set(value) {
            _yesFonte = value
            notifyPropertyChanged(BR.yesFonte)
        }

    var noFonte:  Float
        @Bindable get() = _noFonte
        set(value) {
            _noFonte = value
            notifyPropertyChanged(BR.noFonte)
        }

    var yesDeath:  Int
        @Bindable get() = _yesDeath
        set(value) {
            _yesDeath = value
            notifyPropertyChanged(BR.yesDeath)
        }

    var noDeath:  Int
        @Bindable get() = _noDeath
        set(value) {
            _noDeath = value
            notifyPropertyChanged(BR.noDeath)
        }

    var yesTemperature:  Float
        @Bindable get() = _yesTemperature
        set(value) {
            _yesTemperature = value
            notifyPropertyChanged(BR.yesTemperature)
        }

    var noTemperature:  Float
        @Bindable get() = _noTemperature
        set(value) {
            _noTemperature = value
            notifyPropertyChanged(BR.noTemperature)
        }

    var explication: String?
        @Bindable get() = _explication
        set(value) {
            _explication = value
            notifyPropertyChanged(BR.explication)
        }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeLong(id)
        parcel.writeString(description)
        parcel.writeString(pathImage)
        parcel.writeFloat(yesHealth)
        parcel.writeFloat(noHealth)
        parcel.writeFloat(yesFonte)
        parcel.writeFloat(noFonte)
        parcel.writeInt(yesDeath)
        parcel.writeInt(noDeath)
        parcel.writeFloat(yesTemperature)
        parcel.writeFloat(noTemperature)
        parcel.writeString(explication)
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }

}
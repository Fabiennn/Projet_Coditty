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
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var _id: Long = 0L,

    @ColumnInfo(name = "pseudo")
    private var _pseudo: String? = "",

    @ColumnInfo(name = "score")
    private var _score: Int = 0,

    @ColumnInfo(name = "genre")
    private var _genre: String? = ""
) : Parcelable,
    BaseObservable() {
    var id: Long
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }
    var pseudo: String?
        @Bindable get() = _pseudo
        set(value) {
            _pseudo = value
            notifyPropertyChanged(BR.pseudo)
        }
    var score: Int
        @Bindable get() = _score
        set(value) {
            _score = value
            notifyPropertyChanged(BR.score)
        }
    var genre: String?
        @Bindable get() = _genre
        set(value) {
            _genre = value
            notifyPropertyChanged(BR.genre)
        }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(pseudo)
        parcel.writeInt(score)
        parcel.writeString(genre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
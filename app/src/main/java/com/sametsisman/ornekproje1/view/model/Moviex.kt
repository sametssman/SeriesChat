package com.sametsisman.ornekproje1.view.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class Moviex(val page : Int,
                  val results : List<Result>,
                  val total_pages : Int,
                  val total_results : Int)
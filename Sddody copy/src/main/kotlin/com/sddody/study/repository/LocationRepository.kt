package com.sddody.study.repository

import com.google.type.LatLng
import com.sddody.study.entity.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location,Long>{

}

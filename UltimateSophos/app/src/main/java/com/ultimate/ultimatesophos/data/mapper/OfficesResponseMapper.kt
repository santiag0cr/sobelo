package com.ultimate.ultimatesophos.data.mapper

import com.ultimate.ultimatesophos.data.models.response.OfficeModel
import com.ultimate.ultimatesophos.data.models.response.OfficesResponseModel
import com.ultimate.ultimatesophos.domain.models.OfficeDto
import com.ultimate.ultimatesophos.domain.models.OfficesResponseDto


class OfficesResponseMapper {
    fun transform(value: OfficesResponseModel): OfficesResponseDto {
        return OfficesResponseDto(
            items = transformOfficesModelList(value.items),
            count = value.count,
            scannedCount = value.scannedCount
        )
    }

    private fun transformOfficesModelList(officesList: List<OfficeModel>): List<OfficeDto> {
        return officesList.map { transformOfficesModel(it) }
    }

    private fun transformOfficesModel(office: OfficeModel): OfficeDto {
        return OfficeDto(
            city = office.city,
            longitude = office.longitude,
            id = office.id,
            latitude = office.latitude,
            name = office.name
        )
    }
}

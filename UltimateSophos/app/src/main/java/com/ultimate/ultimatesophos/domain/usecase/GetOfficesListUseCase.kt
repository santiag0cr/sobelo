package com.ultimate.ultimatesophos.domain.usecase

import com.ultimate.ultimatesophos.data.mapper.OfficesResponseMapper
import com.ultimate.ultimatesophos.data.repository.OfficesRepository
import com.ultimate.ultimatesophos.domain.models.OfficesResponseDto


class GetOfficesListUseCase {
    private val repository = OfficesRepository()
    private val mapper = OfficesResponseMapper()

    suspend operator fun invoke(): OfficesResponseDto {
        return mapper.transform(
            repository.getOfficesList()
        )
    }
}

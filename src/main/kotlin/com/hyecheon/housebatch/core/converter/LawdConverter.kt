package com.hyecheon.housebatch.core.converter

import com.hyecheon.housebatch.core.entity.Lawd
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
@Mapper
interface LawdConverter {

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun update(source: Lawd, @MappingTarget target: Lawd)
}
package com.hyecheon.housebatch.job.lawd

import com.hyecheon.housebatch.core.entity.Lawd
import com.hyecheon.housebatch.job.Constant.Exist
import com.hyecheon.housebatch.job.Constant.ExitsTrueValue
import com.hyecheon.housebatch.job.Constant.LawdCd
import com.hyecheon.housebatch.job.Constant.LawdDong
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
class LawdFieldSetMapper : FieldSetMapper<Lawd> {

	override fun mapFieldSet(fieldSet: FieldSet): Lawd {
		return Lawd(
			lawdCd = fieldSet.readString(LawdCd),
			lawdDong = fieldSet.readString(LawdDong),
			exist = fieldSet.readBoolean(Exist, ExitsTrueValue)
		)
	}
}
package com.hyecheon.housebatch.job.lawd

import com.hyecheon.housebatch.core.entity.Lawd
import com.hyecheon.housebatch.core.service.LawdService
import com.hyecheon.housebatch.job.Constant
import com.hyecheon.housebatch.job.validator.FilePathParameterValidator
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/11/28
 */
@Configuration
class LawdInsertJobConfig(
	private val jobBuilderFactory: JobBuilderFactory,
	private val stepBuilderFactory: StepBuilderFactory,
	private val lawdService: LawdService
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun lawdInsertJob() = run {
		jobBuilderFactory.get("lawdInsertJob")
			.incrementer(RunIdIncrementer())
			.validator(FilePathParameterValidator())
			.start(lawdInsertStep())
			.build()
	}

	@Bean
	fun lawdInsertStep() = run {
		stepBuilderFactory.get("lawdInsertStep")
			.chunk<Lawd, Lawd>(1000)
			.reader(lawdFileItemReader())
			.writer(lawdItemWriter())
			.build()
	}

	@Bean
	@StepScope
	fun lawdFileItemReader(@Value("#{jobParameters['${Constant.FilePath}']}") filePath: String? = null) = run {
		FlatFileItemReaderBuilder<Lawd>()
			.name("lawdFileItemReader")
			.delimited()
			.delimiter("\t")
			.names(Constant.LawdCd, Constant.LawdDong, Constant.Exist)
			.linesToSkip(1)
			.fieldSetMapper(LawdFieldSetMapper())
			.resource(ClassPathResource(filePath!!))
			.build()

	}

	@Bean
	@StepScope
	fun lawdItemWriter() = run {
		ItemWriter<Lawd> { lawdService.save(it) }
	}
}
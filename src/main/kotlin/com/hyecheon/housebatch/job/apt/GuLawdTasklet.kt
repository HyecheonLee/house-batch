package com.hyecheon.housebatch.job.apt

import com.hyecheon.housebatch.core.service.LawdService
import com.hyecheon.housebatch.job.Constant
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.repeat.RepeatStatus
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/12/04
 */
open class GuLawdTasklet(
	private val lawdService: LawdService
) : Tasklet {
	companion object {
		const val SearchKey = "guLawdCds"
	}

	override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
		val executionContext = getExecutionContext(chunkContext)
		val guLawdCds = getGuLawdCds(executionContext)
		contribution.exitStatus = getExitStatus(guLawdCds)
		executionContext.putString(Constant.guLawdCd, guLawdCds.poll())
		return RepeatStatus.FINISHED
	}

	private fun getExitStatus(guLawds: Queue<String>) = run {
		if (guLawds.isEmpty()) ExitStatus.COMPLETED
		else ExitStatus("CONTINUABLE")
	}

	private fun getGuLawdCds(executionContext: ExecutionContext): Queue<String> {
		if (!executionContext.containsKey(SearchKey)) {
			executionContext.put(SearchKey, LinkedList(lawdService.guLawdCd()))
		}
		return executionContext.get(SearchKey)!! as Queue<String>
	}

	private fun getExecutionContext(chunkContext: ChunkContext) = run {
		val stepExecution = chunkContext.stepContext.stepExecution
		stepExecution.jobExecution.executionContext
	}
}
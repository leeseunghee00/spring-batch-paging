package io.leeseunghee.jpajdbcpagingtest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BatchController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job jpaPagingBatchJob;

	@Autowired
	private Job jdbcPagingBatchJob;

	/**
	 * 테스트하고자 하는 Job 파라미터를 넣어 실행합니다.
	 * (jpaPagingBatchJob, jdbcPagingBatchJob)
	 */
	@PostMapping("/batch/run")
	public ResponseEntity<String> startBatch() {
		System.out.println("start jdbcPagingBatchJob: " + System.currentTimeMillis());

		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.toJobParameters();

			jobLauncher.run(jdbcPagingBatchJob, jobParameters);
			System.out.println("end jdbcPagingBatchJob: " + System.currentTimeMillis());
			return ResponseEntity.ok("Batch job started successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to start batch job: " + e.getMessage());
		}
	}
}

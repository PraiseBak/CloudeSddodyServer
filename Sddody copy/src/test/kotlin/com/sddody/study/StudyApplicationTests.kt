package com.sddody.study

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.FileInputStream

@SpringBootTest
class StudyApplicationTests {

	@Test
	fun contextLoads() {
		// Firebase 초기화 코드 추가
		if (FirebaseApp.getApps().isEmpty()) {
			initFirebase()
		}
	}

	fun initFirebase() {
		val serviceAccount = FileInputStream("./src/main/resources/sddody-83acb-firebase-adminsdk-f3uqm-7576c7e4ec.json")
		val options: FirebaseOptions = FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.build()
		FirebaseApp.initializeApp(options)
	}
}
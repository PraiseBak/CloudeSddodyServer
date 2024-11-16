package com.sddody.study

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.sddody.study.aop.TimeLogAOP
import com.sddody.study.utility.log.LogTrace
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource

@SpringBootApplication
class StudyApplication
fun main(args: Array<String>) {
	if (FirebaseApp.getApps().isEmpty()) {
		initFirebase()
	}
	runApplication<StudyApplication>(*args)
}

fun initFirebase() {
	val resource = ClassPathResource("sddody-83acb-firebase-adminsdk-f3uqm-7576c7e4ec.json")
	val serviceAccount = resource.inputStream
	val options: FirebaseOptions = FirebaseOptions.Builder()
		.setCredentials(GoogleCredentials.fromStream(serviceAccount))
		.build()
	FirebaseApp.initializeApp(options)
}





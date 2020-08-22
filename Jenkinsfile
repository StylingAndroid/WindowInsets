pipeline {

  agent any

  stages {
      stage('Checkout') {
        steps {
            checkout scm
        }
      }

      stage('Build') {
        steps {
            sh "./gradlew clean assemble"
        }
      }

      stage('Check') {
        steps {
            sh "./gradlew detekt check ktlintCheck"
        }
      }

      stage('Report') {
        steps {
            recordIssues tool: androidLintParser(pattern: '**/reports/**/lint-results.xml', reportEncoding: 'UTF-8')
            recordIssues tool: detekt(pattern: '**/reports/**/detekt.xml', reportEncoding: 'UTF-8')
            recordIssues tool: ktLint(pattern: '**/reports/**/ktlint*.xml', reportEncoding: 'UTF-8')
        }
      }
  }
}
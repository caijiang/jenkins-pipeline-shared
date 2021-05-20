#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 */
def call(String buildStatus = 'STARTED') {
    // build status of null means successful
    buildStatus =  buildStatus ?: 'SUCCESSFUL'

//    def blueOceanDisplayUrlProvider = org.jenkinsci.plugins.displayurlapi.DisplayURLProvider.all().find { it.displayName == "Blue Ocean" }
//
//    String url = blueOceanDisplayUrlProvider?.getJobURL(project) ?: env.BUILD_URL
    String url = (env.BUILD_URL).replaceFirst("/job","/blue/organizations/jenkins").replaceFirst("/job","/detail")

    // Default values
//    def color = 'RED'
//    def colorCode = '#FF0000'
    GString subject = "${env.PROJECT_NAME} - Build # ${env.BUILD_NUMBER} - ${env.BUILD_STATUS}!"
//    def summary = "${subject} (${env.BUILD_URL})"
    GString details = """<summary>${env.PROJECT_NAME} - Build # ${env.BUILD_NUMBER} - ${env.BUILD_STATUS}:</summary>
<p>Check console output at ${env.BUILD_URL} to view the results.</p>
<p>Check console output at $url to view the results.</p>"""

    // Override default values based on build status
    if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
    } else if (buildStatus == 'SUCCESSFUL') {
        color = 'GREEN'
        colorCode = '#00FF00'
    }

    // Send notifications
//    slackSend (color: colorCode, message: summary)
//
//    hipchatSend (color: color, notify: true, message: summary)

//    emailext (
//            to: 'luffy.ja@gmail.com',
//            subject: subject,
//            body: details,
//            recipientProviders: [[$class: 'DevelopersRecipientProvider']]
//    )

    emailext (
            recipientProviders: [developers(), requestor()],
            to: 'luffy.ja@gmail.com',
            subject: subject,
            body: details
    )
}
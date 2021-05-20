#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 */
def call(String buildStatus = 'STARTED') {
    // build status of null means successful
    buildStatus = buildStatus ?: 'SUCCESSFUL'

//    def blueOceanDisplayUrlProvider = org.jenkinsci.plugins.displayurlapi.DisplayURLProvider.all().find { it.displayName == "Blue Ocean" }
//
//    String url = blueOceanDisplayUrlProvider?.getJobURL(project) ?: env.BUILD_URL
    String url = (env.BUILD_URL).replaceFirst("/job", "/blue/organizations/jenkins").replaceFirst("/job", "/detail")

    // 更新内容
    String uls = "<ul>"
    if (env.BRANCH_NAME != null) {
        uls = uls + "<li>目标分支：${env.BRANCH_NAME}</li>"
    }
    if (env.TAG_NAME != null) {
        uls = uls + "<li>目标tag：${env.TAG_NAME}</li>"
    }
    if (env.CHANGE_URL != null) {
        uls = uls + "<li>更新内容：${env.CHANGE_URL}</li>"
    }
    uls = uls + "</ul>"

    // Default values
//    def color = 'RED'
//    def colorCode = '#FF0000'
//    env.BUILD_STATUS
    GString subject = "${env.JOB_BASE_NAME} - 构建状态 #${env.BUILD_NUMBER} - $buildStatus!"
    GString details = """<summary>${env.JOB_BASE_NAME} - 构建状态 #${env.BUILD_NUMBER} - ${buildStatus}:</summary>
$uls
<p>点击<a href="$url">详情</a>查看包括单元测试结果在内的更多构建内容。</p>"""

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

    emailext(
            recipientProviders: [developers(), requestor()],
            to: 'luffy.ja@gmail.com',
            subject: subject,
            body: details
    )
}
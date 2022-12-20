properties([
    parameters([
        choice(name: "LANGUAGE", choices: ["nodejs", "python"], description: "Languages"),
    ])
])
if (params.LANGUAGE == "nodejs") {
env.NODE_NAME = 'nodejs_runner_16'
env.POD_TEMPLATE_NAME = 'nodejs-16'
env.CONTAINER_NAME = 'nodejs-16'	
} else {
    env.MAILGUN_USERNAME = 'api-test'
}

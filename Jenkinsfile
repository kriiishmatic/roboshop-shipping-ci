
@Library('roboshop-ci') _

// Identify the source branch.
// For PR builds, CHANGE_BRANCH contains the source branch.
def sourceBranch = env.CHANGE_BRANCH ?: env.BRANCH_NAME

// Allow only feature branches.
if (sourceBranch?.startsWith('feature/')) {

    echo "Feature branch detected: ${sourceBranch}"
    echo "Starting Shipping CI pipeline..."

    mavenPipeline(

        project: 'project',
        service: 'shipping',

        appRepoUrl: 'https://github.com/kriiishmatic/roboshop-shipping-ci.git',
        appBranch: sourceBranch,

        pomPath: 'pom.xml',

        awsRegion: 'us-east-1',
        ecrRepository: 'project-k/shipping'
    )

} else {

    echo "Skipping pipeline."
    echo "Branch '${sourceBranch}' is not allowed."
    echo "Only feature/* branches and their PRs can execute Shipping CI."
}

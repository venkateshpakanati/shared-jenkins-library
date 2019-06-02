     // def call() {
     //     echo "Test >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
     // }

// #!/usr/bin/groovy
public class test implements Serializable {
    public String podlabel
    public String workingdir
    public String memLmt
    public String cpuLmt
    public String m2FileSystem
    public String buildWrkspace
    def script
    public Map images
    def inputcontainers = []

    public test(String label,
                      Map images,
                      String workingdir,
                      script) {
        this.podlabel=label
        this.workingdir=workingdir
        this.script=script
        this.images = images

      if (images.containsKey('maven')) {
          cpuLmt = '500m'
          memLmt = '500Mi'
          if (images.containsKey('mavenCpuLmt')) {
            cpuLmt = images."mavenCpuLmt"
          }
          if (images.containsKey('mavenMemLmt')) {
            memLmt = images."mavenMemLmt"
          }
          this.inputcontainers  <<
            script.containerTemplate(
              name: 'maven',
              image: images."maven",
              command: 'cat',
              envVars: [
                script.envVar(key: 'JAVA_TOOL_OPTIONS', value: "-Duser.home=${workingdir}"),
                script.envVar(key: 'MAVEN_CONFIG', value: "${workingdir}/.m2")
              ],
              ttyEnabled: true,
              workingDir: workingdir,
              alwaysPullImage: false,
              resourceRequestCpu: '100m',
              resourceLimitCpu: cpuLmt,
              resourceRequestMemory: '500Mi',
              resourceLimitMemory: memLmt
            )
        }
     }

     public void BuilderTemplate (body) {
      script.podTemplate(
          label: podlabel,
          containers: this.inputcontainers,
          volumes: [
            //script.secretVolume(secretName: 'maven-settings', mountPath: "${workingdir}/.m2")
          ]
       ){
            body ()
       }
  }

}






// def call(Closure body) {
//           echo "Test closure body >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
//           echo "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
//           podTemplate(label: 'test-pod', 
//                 containers: [
//                     // containerTemplate(
//                     //      name: 'jnlp',
//                     //      image: 'jenkinsci/jnlp-slave:3.10-1-alpine',
//                     //      args: '${computer.jnlpmac} ${computer.name}'
//                     // ),
//                     containerTemplate(
//                          name: 'maven',
//                          image: 'zenika/alpine-maven',
//                          command: 'cat',
//                          ttyEnabled: true
//                     ),
//                ],
//                volumes: [ 
//                   hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'), 
//                ]
//           )
//      {
//           node('test-pod') {
//                stage ('Checkout Code') {
//                     git branch: 'master',
//                          credentialsId: '35205444-4645-4167-b50e-c65137059f09',
//                          url: 'https://github.com/venkateshpakanati/microservices.git'

//                     sh "ls -lat"
//                     stash name: "first-stash", includes: "**/*"
//                }

//               stage ('Test stage') { 
//                     container('maven') {
//                          echo "under maven >>>>>>>>>>>>>>>>>>>>>>>"
//                          sh 'mvn -version'
//                          dir("first-stash") {
//                               unstash "first-stash"
//                               sh "mvn clean compile"  
//                          }
//                          sh "ls -la ${pwd()}"
//                          sh "ls -la ${pwd()}/first-stash"
//                     }
//                }
//           }
//      }

// }
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//https://github.com/jongmin92/spring-boot-starter-upbit
plugins {
    id("org.springframework.boot") version "2.4.11"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.5.31"
    id("io.freefair.lombok") version "5.3.0"
    //id("org.jetbrains.kotlin.plugin.lombok") version "1.5.31"
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.4.32"
    kotlin("kapt") version "1.4.32"
    //kotlin("plugin.lombok") version "1.5.31"
    kotlin("plugin.spring") version "1.4.32"//kotlin spring插件 这是对kotlin all open插件的一个封装，使具备spring特定注解的类自动转换为open class，以便实现基于aop的功能
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "cn.lnkdoc"
version = "2021.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val snapshotsUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
val release = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"

//extra["springCloudVersion"] = "2020.0.0-M6"
//extra["springCloudAlibabaVersion"] = "2021.1"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

noArg {
    annotation("cn.lnkdoc.auth.annotation.NoArgKt")
    //invokeInitializers = true
}

//版本管理--依赖插件:id("io.spring.dependency-management") version "1.0.11.RELEASE"
dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-starter:2.4.11")
        mavenBom("org.springframework:spring-core:5.3.10")
    }
}
dependencies {
    // import a BOM. The versions used in this file will override any other version found in the graph
    //implementation(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:2.4.11"))
    //implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}"))
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("com.squareup.okio:okio:2.8.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    //--
    implementation("org.springframework:spring-core:5.3.10")
    // https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator
    implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final") {
        //if import bind jakarta.validation-api version under 3.0.0
        //you must manual import : implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    }
    implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    implementation("jakarta.el:jakarta.el-api:4.0.0")
    implementation("org.glassfish:jakarta.el:4.0.1")

    //implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    implementation("javax.validation:validation-api:2.0.1.Final")

    implementation("org.springframework.boot:spring-boot-configuration-processor")
    //implementation("com.jakewharton:butterknife:latest-version")
    //kapt("com.jakewharton:butterknife-compiler:latest-version")
    //该注解处理器：使yaml、和properties配置属性时进行提示。
    //kapt("org.springframework.boot:spring-boot-configuration-processor:2.4.11")

    //implementation("org.springframework.boot:spring-boot-autoconfigure")
    //implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.alibaba:fastjson:1.2.78")

    //implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    //implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    //compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("org.projectlombok:lombok:1.18.22")
    kapt("org.projectlombok:lombok:1.18.20")

    implementation("com.google.guava:guava:31.0.1-jre")
    //implementation("jetty:jetty-util:6.0.2")
    //--url arg --> map
    implementation("org.mortbay.jetty:jetty-util:6.1.26")

    //--uid
    api("com.devskiller.friendly-id:friendly-id:1.1.0")

    //-------------------log
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.6")

    //-------------------annotation processor
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    //-------------------test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

//仓库配置
repositories {
    mavenLocal { setUrl("file://${project.rootDir}/lib") }
    //首先去本地仓库找
    mavenLocal()
    //然后去阿里仓库找
    // build.gradle:
    // maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }

    // build.gradle.kts:
    maven { url = uri("https://repo.spring.io/release") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven {
        isAllowInsecureProtocol = true
        setUrl("https://maven.aliyun.com/nexus/content/groups/public/")
    }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/public") }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/google") }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/spring-plugin") }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
    maven {
        isAllowInsecureProtocol = true
        url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/") }
    google()
    //jcenter()
    //最后从maven中央仓库找
    mavenCentral()
}

publishing {
    //publications.create<MavenPublication>("mavenJava") {
    //    from(components["java"])
    //}
    //repositories {
    //    mavenLocal()
    //}
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("awesome-util")
                description.set("A concise description of awesome-util")
                url.set("http://www.example.com/library")
                properties.set(mapOf(
                    "myProp" to "value",
                    "prop.with.dots" to "anotherValue"
                ))
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https:///www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("langkye")
                        name.set("langkye")
                        email.set("langkye@gmail.com")
                    }
                }
                scm {
                    //connection.set("scm:git:git://example.com/my-library.git")
                    connection.set("scm:git:git:git@github.com:lnkdoc/datax-plugin-txtmultipledelimiterwriter.git")
                    //developerConnection.set("scm:git:ssh://example.com/my-library.git")
                    developerConnection.set("scm:git:ssh:https://github.com/lnkdoc/datax-plugin-txtmultipledelimiterwriter.git")
                    url.set("https://www.github.com/lnkdoc/datax-plugin-txtmultipledelimiterwriter")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        create("CenterNexus") {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set("langkye") // defaults to project.properties["myNexusUsername"]
            password.set("your-password") // defaults to project.properties["myNexusPassword"]
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

//打包任务构建时，生成javadoc
tasks.withType<Jar> {
    manifest {
        attributes["Multi-Release"] = "true"
    }
    //from(sourceSets["main"].allSource) {
    //    excludes.contains("**/plugin_job_template.json")
    //}
    //from(sourceSets["test"].allSource) {
    //    excludes.contains("**/plugin_job_template.json")
    //}
    //println("####Will be generate Java's documents")
    from(tasks["javadoc"]) {
        into("/javadocs")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
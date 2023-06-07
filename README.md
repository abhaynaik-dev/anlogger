<div align="center">
  <br>
  <h1> 📝  ANLogger 📝 </h1>

<img src="https://img.shields.io/badge/api-21%2B-yellow"/> <img src="https://img.shields.io/badge/ui-jetpackcompose-blue"/>

  <p>This was a simple module that I used in the majority of my applications; it is now a library. A simple way to retrieve logs from a device without requiring adb access or external read/ write permission, as well as an easy way to send the log file via email. It helps me a lot, hope it does to you too. </p>
  <strong>Happy Debugging !!!</strong>
</div>

#  📖 Prerequisite
1) Use [Timber][1] as a default logging library.
2) Do not plant or use any custom a `Timber` tree, `ANLogger` will take care of everything.

# ⭕ Usage
<img src="https://github.com/abhaynaik-dev/anlogger/blob/feature-share-file-ui/githubcontent/sample_anlogger.gif" align="right" width="250px" />

1) Dependency
```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation "io.github.abhaynaik-dev:anlogger:0.0.2"
}
```
2) Configure ANLogger
```kotlin
//Use this in onCreate() of application class
ANLogger.setup(this, "<TAG>" ,ANLoggingLevel.AN_FILE_LOGGING)
//Logging levels
//AN_NONE - To be used in production mode
//AN_FILE_LOGGING - To write Timber logs in file
//AN_ADB_LOGGING - Default Timber.DebugTree() usage, must be used in debug mode only
```
3) Share single log file
> This should be used only with `AN_FILE_LOGGING` logging level\
> This API will show a dialog with list of available log files. User can select a file and share it via mail
```kotlin
ANLogger.shareLogFile(context = this)
```
4) Share all log files at once
> This should be used only with `AN_FILE_LOGGING` logging level\
> This API will share all available log files via mail
```kotlin
ANLogger.shareAllLogFiles(context = this)
```

5) Delete all log files
> This should be used only with `AN_FILE_LOGGING` logging level\
> This API will delete all availble log files
```kotlin
ANLogger.clearLogFiles()
```

[1]:https://github.com/JakeWharton/timber

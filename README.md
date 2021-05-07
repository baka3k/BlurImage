# Blur Photo
To make your imageview blurry on Android,
Library using rendersript which is most efficient for doing these tasks and provide best performance. 
(Optional by using CPU to blur task)

Good luck !!!

## Table of contents

- [Features](#features)
- [Requirements](#requirements)
- [Usage](#usage)
- [Sample](#sample)
- [Authors](#authors)
- [License](#license)

## Features

- [x] Blur photo from resource, or file
- [x] Blur view
- [x] Optional Blur by using CPU or Render Script

## Requirements

- Android 5.1+

## Gradle
build.gradle:
```groovy
allprojects {
    repositories {
        .....
        maven { url 'https://jitpack.io' } // add this line to build.gradle
    }
}
```
Add the dependency
```groovy
dependencies {
    implementation "com.github.baka3k:BlurImage:1.0.1"
}
```
## Usage
Load image from resource, and draw blur image to Imageview
```Kotlin
 BlurImage.getInstance(applicationContext)
                .load(R.raw.a) //from resource or image path from storage
                .radius(22F)
                .withRenderScript() // or .withCPU()
                .into(imageView)
```

Capture Screen, blur then draw image to Imageview
```Kotlin
private lateinit var rootView: View
.....
rootView = findViewById(R.id.rootView)
.....
 BlurImage.getInstance(applicationContext).load(rootView)
                .radius(20f)
                .withRenderScript()
                .into(imageView)
```

## Sample

![Output sample](https://github.com/baka3k/BlurImage/blob/main/sample.gif)

refer sample in below package
```Java
com.baka3k.blur.example.MainActivity

```
## Authors

baka3k@gmail.com

## License
MIT licensed

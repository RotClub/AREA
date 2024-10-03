FROM ubuntu:latest AS kotlin

WORKDIR /build

# Install necessary packages and dependencies
RUN apt-get update \
    && apt-get install -y \
        wget \
        unzip \
        git \
        openjdk-17-jdk \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables for SDK
ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Download and install Android SDK command-line tools
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools \
    && cd ${ANDROID_SDK_ROOT}/cmdline-tools \
    && wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip -O commandlinetools.zip \
    && unzip commandlinetools.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools \
    && mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest \
    && rm commandlinetools.zip

# Install SDK packages
RUN yes | sdkmanager --licenses \
    && sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Copy the project files into the container
COPY . .

# Replace local.properties with the correct path
RUN echo "sdk.dir=${ANDROID_SDK_ROOT}" > local.properties

# Build the project with gradle wrapper
RUN chmod +x ./gradlew
RUN ./gradlew :app:assembleRelease

FROM alpine:latest

WORKDIR /app

COPY --from=kotlin /build/app/build/outputs/apk/release/app-release-unsigned.apk /app/app.apk

CMD ls -lR /app

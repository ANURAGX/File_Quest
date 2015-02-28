### 
#  Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
#  
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#  
#       http://www.apache.org/licenses/LICENSE-2.0
#       
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
#                              
#                              anuraxsharma1512@gmail.com
# 

 


LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4
LOCAL_STATIC_JAVA_LIBRARIES := Dropbox-httpmime-4.0.3
LOCAL_STATIC_JAVA_LIBRARIES := Dropbox-json-simple-1.1
LOCAL_STATIC_JAVA_LIBRARIES := Dropbox-SDK-1.6.1
LOCAL_STATIC_JAVA_LIBRARIES := Google-Play-Services
LOCAL_STATIC_JAVA_LIBRARIES := Image-Viewer
LOCAL_STATIC_JAVA_LIBRARIES := Root-Tools
LOCAL_PACKAGE_NAME := File Quest
LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))
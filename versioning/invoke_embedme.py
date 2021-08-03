# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.

# Python version 3.4 or higher is required to run this script.



import argparse
import os
import sys
import urllib.request

# NPX command.
npx_command = 'npx embedme'

def delete_from_file(the_file):
    if os.path.exists(the_file):
      os.remove(the_file)

def main():
    # load file from azure-sdk-for-java
    keyVaultJcaManagedIdentitySample_path = os.path.abspath("../keyvault/azure-spring-boot-sample-keyvault-certificates-client-side/src/main/java/com/azure/spring/security/keyvault/KeyVaultJcaManagedIdentitySample.java")
    url = r"https://raw.githubusercontent.com/Azure/azure-sdk-for-java/main/sdk/spring/azure-spring-boot/src/samples/java/com/azure/spring/keyvault/KeyVaultJcaManagedIdentitySample.java"
    content_from_url =  urllib.request.urlopen(url).read().decode('utf-8')

    with open(keyVaultJcaManagedIdentitySample_path, "w" ) as f:
        f.write(content_from_url)

    command = npx_command

    readmePath = "../keyvault/azure-spring-boot-sample-keyvault-certificates-client-side/README.md"
    # If the passed README path was relative
    print(os.path.abspath(readmePath))
    command += ' ' + os.path.abspath(readmePath)

    os.system(command)

    delete_from_file(keyVaultJcaManagedIdentitySample_path)

    sys.exit()

if __name__ == '__main__':
    main()

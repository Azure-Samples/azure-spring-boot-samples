# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.

# Python version 3.4 or higher is required to run this script.



import argparse
import os
import sys

# NPX command.
npx_command = 'npx embedme'



def main():
    command = npx_command

    readmePath = "../keyvault/azure-spring-boot-sample-keyvault-certificates-client-side/README.md"
    # If the passed README path was relative
    print(os.path.abspath(readmePath))
    command += ' ' + os.path.abspath(readmePath)

    sys.exit(os.system(command))

if __name__ == '__main__':
    main()

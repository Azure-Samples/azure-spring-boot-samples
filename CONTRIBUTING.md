## How to Develop Codes to This Repo


### 01. Contribute to Main Branch

1. Make your changes in a new git fork.
2. Checkout a new  feature/bugfix branch from `main`.
   > The `main` branch is a develop branch, the samples may depend on beta version libraries, which are not released to maven central.

3. Develop in feature/bugfix branch.
4. Build the unreleased dependency libraries in your local machine.
   ```bash
      git init azure-sdk-for-java
      cd azure-sdk-for-java
      git remote add origin https://github.com/Azure/azure-sdk-for-java.git
      git config core.sparsecheckout true
      echo "sdk/spring" >> .git/info/sparse-checkout
      echo "eng" >> .git/info/sparse-checkout
      echo "sdk/keyvault" >> .git/info/sparse-checkout
      echo "sdk/boms" >> .git/info/sparse-checkout
      git pull --depth=1 origin feature/azure-spring-cloud-4.0
      mvn clean install -Dmaven.javadoc.skip=true -DskipTests \
          -Dcheckstyle.skip=true \
          -ntp \
          -Dspotbugs.skip=true \
          -Drevapi.skip=true -Djacoco.skip=true \
          -Dparallel-test-playback \
          -Pdev \
          -f sdk/spring/pom.xml
   ```
5. Make pull request to merge the branch into `main`.

   > There are github actions to check some status here.

### 02. Contribute to Other Branch Except Main Branch

1. Make your changes in a new git fork.
2. Checkout a new  feature/bugfix branch from current branch.
3. Develop in feature/bugfix branch.
5. Make pull request to merge the branch into current branch.

   > There are github actions to check some status here.

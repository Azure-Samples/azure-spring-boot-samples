## How to Develop Codes to This Repo


### 01. Contribute to Main Branch

1. Make your changes in a new git fork.
2. Checkout a new  feature/bugfix branch from `main`.
   > The samples currently use released Spring Cloud Azure dependencies from Maven Central. CodeQL builds the samples directly without building the Azure SDK repository.

3. Develop in feature/bugfix branch.
4. Build and verify the samples with the JDK version specified in the CI workflow.
   ```bash
      mvn -f pom.xml --batch-mode --update-snapshots verify
   ```

   Only if you change a sample to use an unreleased dependency, build the matching Azure SDK source branch locally first. Set `SDK_BRANCH` to the branch that provides that dependency:
   ```bash
      git init azure-sdk-for-java
      cd azure-sdk-for-java
      git remote add origin https://github.com/Azure/azure-sdk-for-java.git
      git config core.sparsecheckout true
      echo "sdk/spring" >> .git/info/sparse-checkout
      echo "eng" >> .git/info/sparse-checkout
      echo "sdk/keyvault" >> .git/info/sparse-checkout
      echo "sdk/boms" >> .git/info/sparse-checkout
      git pull --depth=1 origin "${SDK_BRANCH:?Set SDK_BRANCH to the matching Azure SDK branch}"
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

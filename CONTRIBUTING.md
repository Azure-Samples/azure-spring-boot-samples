## Github actions of this repo

There are several github actions in this repo to do some basic checks with pull requests or branches.

| Action Name                                                  | Note                         |
| ------------------------------------------------------------ | ---------------------------- |
| [codeql-analysis](https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/.github/workflows/codeql-analysis.yml) | Code analysis                |
| [check-style](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/.github/workflows) | Java check style             |
| [check-markdown-links](https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/.github/workflows/check-markdown-links.yml) | Check broken markdown links. |
| [azure-spring-boot-samples-action](https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/.github/workflows/azure-spring-boot-samples-action.yml) | Maven build check.           |


## How to develop codes to this repo.


### 01. Develop sample with the latest released libraries

In this scenario,  the samples' dependency has been released to maven central.

1. Make your changes in a new git fork.
1. Checkout a new  feature/bugfix branch from `main`.
2. Develop in feature/bugfix branch.
3. Make pull request to merge the branch into main.(able to merge)


### 02. Develop sample with released versions of libraries(not the latest version)

For some reason, we may need to provide sample with not the latest library but specific version of libraries.

In this scenario:
1. Make your changes in a new git fork.
1. Checkout a new feature/bugfix branch.
2. Specify the version of libraries.
3. Develop in feature/bugfix branch.
3. Make pull request to merge the branch into new **feature** branch.

### 03. Develop sample with unreleased libraries

In this scenario, the samples may depend on beta version library, which is not released to maven central.

1. Make your changes in a new git fork.
1. Checkout a new  feature/bugfix branch from `main`.

2. Develop in feature/bugfix branch.

3. Before the dependent library is released to maven central.

    - Test sample in local machine.

4. After the dependent library is released to maven central.

    - Make pull request to merge the branch into `main`.

      > There are github actions to check some status here,
      >
      > if the dependent library is not released to maven central, the pull request will be blocked to merge.
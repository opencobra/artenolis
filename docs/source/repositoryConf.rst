Repository configurations
=========================

Repository structure
--------------------

The test suite is located in `/test`, while the source files of the code base are
located in the `/src` directory. A common minimial repository structure is given as:

=============  =======================================================
 Directory      Purpose
=============  =======================================================
/.artenolis     Directory with continuous integration bash-scripts
/src            Directory with code source files
/test           Directory with test files and testAll.m
artenolis.yml   YAML trigger script
codecov.yml     YAML script for code coverage report
=============  =======================================================

Development model
------------------

A common development model of a stable `master` branch and a `develop` branch shall be chosen for development,
which is particularly well suited for a reproducibility and testing infrastructure, such as ARTENOLIS.
It is against the `develop` branch that new pull requests are raised by developers, while it is the
`master` branch that contains the stable version of the code base. A regular merge strategy from the
`develop` to the `master` branch ensures that the latest features are adopted in the stable version.
As the development of new features is made on separate branches that are being merged to the `develop`
branch only through Pull Requests (PRs), and as each PR is being tested by the continuous integration system,
the risk of the `develop` branch failing to build is very low. This stability of the `master` branch is
particularly important in a fast-moving research environment that relies on the reproducibility of data-driven results.



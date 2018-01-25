Continuous integration scripts
==============================

When triggered, each job starts by cloning the repository, and checks out the latest commit or the commit that shall be built.
On the Jenkins node, the Java web call triggers the interpretation of a YAML (YAML Ain’t Markup Language) script, namely the
`artenolis.yml` script.


.. code-block:: yaml

    language: bash

    before_install:

        # fresh clone of the repository
        - if [[ -a .git/shallow ]]; then
            git fetch --unshallow;
          fi

    script:

        # launch the tests
        - bash .ci/runtests.sh


Together with build-specific environment variables, an executable shell script, also known as the Hudson shell file, is generated.
This Hudson shell file is then sent to and executed on each slave in a shell-like environment. The continuous integration process
is consequently started through the `artenolis.yml` script placed at the root of the repository directory.
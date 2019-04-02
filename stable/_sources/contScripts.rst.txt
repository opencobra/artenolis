Continuous integration scripts
==============================

When triggered, each job starts by cloning the repository, and checks out the latest commit or the commit that shall be built.
On the Jenkins node, the Java web call triggers the interpretation of a YAML (YAML Ain’t Markup Language) script, namely the
`.artenolis.yml` script.


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


Together with build-specific environment variables, an executable shell script,
also known as the Hudson shell file, is generated.  This Hudson shell file is
then sent to and executed on each slave in a shell-like environment. The
continuous integration process is consequently started through the
`.artenolis.yml` script placed at the root of the repository directory.

As a shell script offers cross-platform flexibility, a proper shell script is
called from the YAML script, namely `runtests.sh` located in the specific `.artenolis`
folder. The shell script `runtests.sh` runs on all
supported platforms. 

Each platform is identified by the environment variable
`ARCH`, which is set by the job definitions. The simplest
launch command is set for UNIX operating systems (Linux and macOS).

Setup on Windows
----------------

A key challenge is to trigger a build on a Windows platform and launch MATLAB
while providing live feedback, similar to UNIX platforms. As no native bash
console exists on DOS-based systems, the output of MATLAB is not directly
routed to the console. Instead, `git Bash <git-scm.com/download/win>`_ is used,
and the Hudson script launching MATLAB is executed in `sh.exe`. As the output
from a shell-like environment is not routed back to the master node directly, a
computational trick must be used in order to however display a live console
output on the Jenkins web interface. 

This trick ensures the homogeneity of `ARTENOLIS` despite the large differences
between DOS and UNIX operating systems supported by `ARTENOLIS`.

The output of MATLAB is routed to a file (`output.log`), while the process is
run as a hidden process marked with &. The process ID is saved as PID. While
the hidden process is running, the log file is constantly read in (or followed)
by the system command tail, whose output is redirected to Jenkins. The shell
script runtests.sh then only exits once the MATLAB process with PID has been
executed without an error.

Any eventual error code thrown during this process is caught in the variable
`$CODE`. This is the exit code of the script `runtests.sh` that is returned to
Jenkins running on the master node.

The `runtests.sh` script
------------------------

.. code-block:: bash

    #!/bin/sh
    if [ "$ARCH" == "Linux" ]; then
        $INSTALLDIR/MATLAB/$MATLAB_VER/bin/./matlab -nodesktop -nosplash < test/testAll.m

        elif [ "$ARCH" == "macOS" ]; then caffeinate -u &
            $INSTALLDIR/MATLAB_$MATLAB_VER.app/bin/matlab -nodesktop -nosplash < test/testAll.m

        elif [ "$ARCH" == "windows" ]; then
            # change to the build directory
            echo " -- changing to the build directory --"
            cd "D:\\jenkins\\workspace\\COBRAToolbox-windows\\MATLAB_VER\\$MATLAB_VER\\label\\$ARCH"
            echo " -- launching MATLAB --"
            unset Path
            nohup "D:\\MATLAB\\$MATLAB_VER\\\bin\\matlab.exe" -nojvm -nodesktop -nosplash -useStartupFolderPref -logfile output.log -wait -r "restoredefaultpath; cd test; ... testAll;" & PID=$!

            # follow the log file
            tail -n0 -F --pid=$! output.log 2>/dev/null
            # wait until the background process is done
            wait $PID 
        fi

    CODE=$? 
    exit $CODE



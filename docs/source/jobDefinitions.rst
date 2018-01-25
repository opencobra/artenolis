Job definitions
===============

A job is a configuration of a build pipeline on ARTENOLIS, has a specific purpose, runs on a different slave/operating system,
or builds a different branch of the repository. An example of such a configuration file is given under
artenolis.lcsb.uni.lu/configExample.yml.

In essence, one job is defined per operating system. Commonly, one job is defined per slave in order to provide
the highest robustness of ARTENOLIS. A job may also be parameterised. It is possible to configure a matrix of sub-jobs
for different MATLAB versions using the `MATLAB_VER` parameter.

In order to streamline the continuous integration setup, two job types may be distinguished:
    - jobs that trig- ger builds of the `develop` and `master` branches (marked with `-branches`)
    - jobs marked with `-pr` that build any newly submitted pull request (PR).

Each job type is either triggered automatically by Jenkins or manually by an administrator using the SHA1 of the commit
in order to ensure flexibility of the continuous integration system and in case of emergency.

Another good practice is to configure a job for testing purposes
(e.g., a job that only builds a specific branch) before making any production changes on critical jobs (i.e., the `-branches` jobs).

Each job can either succeed or fail. The build stability of each job and build trend is monitored and can be retrieved from
prince.lcsb.uni.lu/jenkins/job/<jobName>/buildTime-Trend, where `<jobName>` is the name of a job.


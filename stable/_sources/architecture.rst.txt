Architecture
============

Computing nodes and resources
-----------------------------

The advantage of virtual machines over physical ma- chines is that various
computing environments can co-exist on a single physical machine. A setup with
fewer physical machines but with multiple virtual environments is more
economical and occupies a smaller space in the server racks. Thanks to a
virtualisation layer and a hypervisor monitoring the health, new virtual
machines can be created or deleted on demand and based on the available
capacity of the physical server. 

The Jenkins virtual machine running the Linux Ubuntu operating system is a
shared resource, is referred to as the master node, handles HTTPs requests, and
orchestrates the slave nodes.  The Linux (Debian based), Windows 7, and Windows
10 operating systems are virtual machines and are running on the same physical
computing node, whereas the macOS operating system is running on a dedicated
physical machine. The specifications of the computing nodes are:

= =========== ======== ======== ========= ===== ============= =============
#  Name       Type     Mode     Memory    cores  OS            Storage (GB)
= =========== ======== ======== ========= ===== ============= =============
1  Prince     Virtual  master   8GB ECC   4      Ubuntu 16.04   34+120
2  Linux      Virtual  slave    18GB ECC  20     Ubuntu 16.04   46+200
2  Windows 7  Virtual  slave    18GB ECC  20     Windows 7      60+200
2  Windows 10 Virtual  slave    18GB ECC  20     Windows 10     50+200
3  Mac        Physical slave    24GB ECC  12     macOS 10.12    250
= =========== ======== ======== ========= ===== ============= =============

A limited amount of storage is usually provided to each virtual machine and
only satisfies the need to run and store small amount of data. For larger data,
such as build data, an NFS (Network File System) or SMB (Server Message Block)
mount point is provided on a central storage system.

Virtual machine management and access control
---------------------------------------------

Security and appropriate access control to each virtual machine and associated
services is provided through `FreeIPA <http://freeipa.org>`_. FreeIPA provides
a centralised resource to control authentication, authorisation and account
information by storing all information about a user, virtual machine and other
sets of objects required to manage the security of the virtual machine.

In order to reduce maintenance and initialisation time of new virtual machines,
the virtual machines are deployed using `Foreman <http://theforeman.org>`_, a virtual-
isation environment agnostic web tool typically used to manage the complete
lifecycle of virtual machines.

All physical and virtual servers (except the macOS node) are configured via
Puppet, which is a configuration management tool that facilitates standard-
ised configurations across a pool of servers such as firewall definitions,
administration SSH (Secure Shell) keys, default packages, and other settings.
In the current setup, the compliance of all machine configurations is
constantly monitored, while periodic health reports are provided to Foreman.
Besides the configuration monitoring and deployment, the performance of the
virtual machines is monitored using `netdata
<http://github.com/firehol/netdata>`_, which is particularly useful when
evaluating the performance of the continuous integration test suite.


Virtualisation layer
--------------------

Hypervisor software runs on a server handling virtual machines (VMs). In the
present case, `oVirt <http://ovirt.org>`_ is installed on each of the physical
servers, and is a key element in VM management. Kernel-based Virtual Machine
(KVM), a virtualisation infrastructure, is used as the hypervisor layer.
`oVirt` provides a graphical user interface (GUI) to manage all physical and
logical resources needed for the virtual infrastructure (e.g storage, network,
data centres).  In the current continuous integration system, the 2 physical
servers (not the macOS node) are running CentOS 7.3 and are virtualised using
oVirt 4.1.0.

As reported in Bug 497356 logged on Bugzilla:

https://bugs.eclipse.org/bugs/show_bug.cgi?id=497356

The solution was to deleted the following files from the META-INF folder of the "org.eclipse.birt.runtime_4.6.0-20160607.jar" archive:

ECLIPSE_.RSA
ECLIPSE_.SF

https://www.eclipse.org/forums/index.php/t/1078982/

zip -d org.eclipse.birt.runtime_4.6.0-20160607.jar 'META-INF/*.SF' 'META-INF/*.RSA'
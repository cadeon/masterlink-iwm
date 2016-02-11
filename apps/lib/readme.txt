Commons-beanutils.jar and commons-digester.jar
must be present in web lib in addition to ear level.
This is due to Jboss hot deplyment issues. If digester left on ear level only the app loads up
with no errors firts time, butany consecutive hot deployments results in classpath errors such as validator resources not found.
Likely the hot deployment is done by a different classloader which is not aware of web module classpath.
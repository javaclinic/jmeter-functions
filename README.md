jmeter-functions
================

* Simple JMeter reusable functions.



Useful classes
--------------
* `RandomDate`
* `RandomChoice`
* `RandomLong`
* `RandomInteger`



How to use in JMeter
--------------------
* In your HTTP Request, as part of the body or headers, use Java variables, e.g.
```
${__RandomChoice(Home,Work,Fax,Cell,Other)}
${__RandomDate()}
${__RandomDate(seconds)}
${__RandomDate(start,end)}
${__RandomInteger()}
${__RandomInteger(max)}
${__RandomInteger(min,max)}
${__RandomLong()}
${__RandomLong(max)}
${__RandomLong(min,max)}
```


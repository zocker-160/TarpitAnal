# Tarpit Analyser

Analyse your `tarpit.log` because why not.

### Requirements

- Java 11 or up

## Usage

```bash
$ java -jar TarpitAn.jar tarpit.log
```

Your `tarpit.log` should look like this:

```log
2021-05-27 00:47:41 INFO     TarpitServer: Client ('154.160.0.156', 39171) connected
2021-05-27 00:48:28 INFO     TarpitServer: Client ('61.177.173.9', 23800) connected
2021-05-27 00:48:59 INFO     TarpitServer: Client ('154.68.5.161', 31716) disconnected
2021-05-27 00:49:01 INFO     TarpitServer: Client ('154.68.5.161', 31717) connected
2021-05-27 00:49:29 INFO     TarpitServer: Client ('154.160.0.156', 39171) disconnected
2021-05-27 00:49:31 INFO     TarpitServer: Client ('154.160.0.156', 39172) connected
2021-05-27 00:49:39 INFO     TarpitServer: Client ('61.177.173.9', 55640) connected
2021-05-27 00:49:43 INFO     TarpitServer: Client ('222.187.239.109', 42317) connected
2021-05-27 00:50:45 INFO     TarpitServer: Client ('61.177.173.9', 59270) connected
2021-05-27 00:51:19 INFO     TarpitServer: Client ('154.160.0.156', 39172) disconnected
2021-05-27 00:51:25 INFO     TarpitServer: Client ('154.160.0.156', 39174) connected
2021-05-27 00:51:50 INFO     TarpitServer: Client ('61.177.173.9', 44106) connected
2021-05-27 00:52:52 INFO     TarpitServer: Client ('61.177.173.9', 31746) connected
```

Expected output:
```bash
InputFile: tarpit.log
---
Total parsed entries: 646297
Connected: 323717
Disconnected: 322580
Time in total: 19655 days (53.849316 years)
Average: 1h 27m 44s 
Longest: 109 days (0.29863015 years)
Shortest: 1s 
----
Parsing time: 1015ms
Calculation time: 234ms
Print time: 21ms
TOTAL time: 1273ms
```

# botlist-updater

A small script that periodically updates the server count of your Discord bot on [top.gg](https://top.gg).

## Usage

```
$ git clone https://github.com/JohnnyJayJay/botlist-updater
```

Then create a file `botlist-updater/config.edn` with the following contents:

```clojure
{:discord-token "YOUR-BOT-TOKEN"
 :dbl-token "YOUR-top.gg-TOKEN"
 :period 5} 
```

Your Discord bot token can be found on its [application page](https://discord.com/developers/applications). Your top.gg token can be found in the webhooks settings of your bot. The period denotes how many minutes to wait between updates.

When you're done with the configuration, simply run 

```
$ docker-compose up
```

to start the app.

## License

Copyright © 2021 JohnnyJayJay

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

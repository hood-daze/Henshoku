# Henshoku
エドワードゴーリーの作品から着想を得たノベルゲームです。 Unityなどのゲームエンジンを使わず、kotlinで直に書きました。 

本アプリで工夫したところについて。

コードについてストーリーの基盤となるデータを初期化するのに、デザインパターンのシングルトンパターンを使いました。

またゲーム機能のひとつとして、オートセーブ機能をつけました。これはActivityが破棄されるタイミングにセーブをすることで
実現しました。

成果物のURLはこちらです。https://play.google.com/store/search?q=pub%3Ahood&c=apps&hl=ja 

# fact: A sample of Clojurescript + Cloudflare Workers

## Usage

### Build

```
npx shadow-cljs release worker
```

### Deploy

```
npm run deploy
```

### Access

```
curl -X GET https://fact.contellas.workers.dev/fact?n=10

{"n":10,"fact":3628800}
```

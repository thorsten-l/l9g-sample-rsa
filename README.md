# l9g-sample-rsa

A very simple example for RSA encryption and decryption to explain the concept.

## Example

One simple example

### Definitions

def. p, q, e are prime numbers

def. p != q;  p > 1; q > 1

### random prime numbers for p and q

```t
p=61  q=23

N=p*q=61*23=1403  pN=(p-1)*(q-1)=60*22=1320
```

### Finding e

def. e and pN teilerfremd -> ggT(e,pN) == 1

```t
1320= 2[660] 2[330] 2[165] 3[55] 5[11] 11[1]

e=13

public key = [13, 1403]
```

### Finding d

|   |     e |    pN |     x |     R |     a |     b | until R = 0 |
|---|-------|-------|-------|-------|-------|-------|-|
| 1 |    13 |  1320 |     0 |    13 |     0 |     0 | e1=13; pN1=1320; x1=e1/pN1; R1=e1 mod pN1 |
| 2 |  1320 |    13 |   101 |     7 |     0 |     0 | e2=pN1; pN2=R1; x2=e2/pN2; R2=e2 mod pN2 |
| 3 |    13 |     7 |     1 |     6 |     0 |     0 | e3=pN2; pN3=R2; x3=e3/pN3; R3=e3 mod pN3 |
| 4 |     7 |     6 |     1 |     1 |     0 |     0 | e4=pN3; pN4=R3; x4=e4/pN4; R4=e4 mod pN4 |
| 5 |     6 |     1 |     6 |     0 |     0 |     0 | e5=pN4; pN5=R4; x5=e5/pN5; R5=e5 mod pN5 |
| | **e** | **pN** | **x** | **R** | **a** | **b** | **and back** |
| 5 |     6 |     1 |     6 |     0 |     0 |     1 | b5=1 |
| 4 |     7 |     6 |     1 |     1 |     1 |    -1 | b4=a5-(x4*b5); a4=b5 |
| 3 |    13 |     7 |     1 |     6 |    -1 |     2 | b3=a4-(x3*b4); a3=b4 |
| 2 |  1320 |    13 |   101 |     7 |     2 |  -203 | b2=a3-(x2*b3); a2=b3 |
| 1 |    13 |  1320 |     0 |    13 |  -203 |     2 | b1=a2-(x1*b2); a1=b2 |

`d = 1320 + -203`

def. 0 < d < pN  ->  0 < 1117 < 1320

```t
d=1117

private key = [1117, 1403]
```

## Encryption

def. 0 < M < N  ->  0 < 920 < 1403

```t
M=920

920^13 = 338253076642491662461829120000000000000 [39] Numbers

C= M^e mod N = 920^13 mod 1403 = 161
```

## Decryption

```t
C=161

161^1117 = 105804350996650498558145800131537630... [2466] Numbers

M= C^d mod N = 161^1117 mod 1403 = 920
```

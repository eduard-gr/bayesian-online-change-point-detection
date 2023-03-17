# About library
This library was made on the basis of the [Bayesian Online Changepoint Detection](https://arxiv.org/abs/0710.3742) and 
[Conjugate Bayesian analysis of the Gaussian distribution](https://www.cs.ubc.ca/~murphyk/Papers/bayesGauss.pdf) articles. 
There are quite a lot of similar libraries on the github. 
The primary purpose of writing this library was to understand the work of the Baysian model in solving the problem determining the point of change.

## Methods for solving the problem Online Change Point Detection
| Name | Method | Reference |
| --- | --- | --- |
| amoc | At Most One Change | Hinkley (1970) |
| binseg | Binary Segmentation | Scott and Knott (1974) |
| bocpd | Bayesian Online Change Point Detection | Adams and MacKay (2007) |
| bocpdms | bocpd with Model Selection | Knoblauch and Damoulas (2018) |
| cpnp | Nonparametric Change Point Detection | Haynes et al. (2017) |
| ecp | Energy Change Point | Matteson and James (2014) |
| kcpa | Kernel Change-Point Analysis | Harchaoui et al. (2009) |
| pelt | Pruned Exact Linear Time | Killick et al. (2012) |
| prophet | Prophet | Taylor and Letham (2018) |
| rbocpdms | Robust bocpdms | Knoblauch et al. (2018) |
| rfpop | Robust Functional Pruning Optimal Partitioning | Fearnhead and Rigaill (2019) |
| segneigh | Segment Neighborhoods | Auger and Lawrence (1989) |
| wbs | Wild Binary Segmentation | Fryzlewicz (2014) |
| zero | No Change Points | |

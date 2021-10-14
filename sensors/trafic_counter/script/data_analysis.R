dat = read.csv("data/trafic.csv", header = FALSE, sep=";")
size <- nrow(dat)
mat<-matrix(nrow=nrow(dat),ncol=3)

mat_res<-matrix(nrow=nrow(dat),ncol=3)


delta <- 3000 / 50

for ( i in 1:size)
{
  mat[i,1] <- dat[i,2]
  j <- 0;
  
  if(dat[i,1] < 970)
    j <- dat[i,1]
  mat[i,2] <- j
}

mat[,3] <- 0
for ( i in 1:(size-2))
{
  if(mat[i,2]!=0 && mat[i+1,2]!=0)
  {
    if(mat[i,2]!=0 && mat[i+2,2]==0)
    {
      if(abs(mat[i,2] - mat[i +1 ,2]) > 200)
        mat[i,3] <- 3
      else if(abs(mat[i,2] - mat[i +1 ,2]) > 100)
            mat[i,3] <- 2
      else
        mat[i,3] <- 1
      mat[i+1,2] <- 0
    }
  }
}


mat_res[1,] <- mat[1,]

for ( i in 2:size)
{
  res <- 0
  mat_res[i,1] <- mat[i,1]
  mat_res[i,] <- mat[i,]
  if(abs(mat[i,2] - mat[i-1,2]) > 70)
  {
    res <- mat[i,2];
  }
  mat_res[i,2] <- res
}

mat_res <- mat
for ( i in 1:(size-2))
{
  print(paste("i=",i," size=",size))
  for(j in (i+1):min(i+delta+ 1, size))
  {
    if(40 > abs(mat_res[i,2] - mat_res[j,2]) )
      mat_res[j,2] <- 0;
  }
}


plot(mat_res)


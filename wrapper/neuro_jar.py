import secrets
import os
import pandas as pd

class NeuroJar():

    def __init__(self, id=0,seed=103,epochs=100000):
        self.id = id
        self.jar = "C:\\Users\\joalc\\Dropbox\\Doctorado\\Programas\\cellular-processing-neuroevolution\\out\\artifacts\\CPA_jar\\CPA.jar"
        self.results_path = "C:\\Users\\joalc\\Dropbox\\Doctorado\\PaperEureka\\3-Uploaded Data\\HUO\\PADEL\\neuro_runs"
        self.seed = seed
        self.epochs = epochs
        while(True):
            self.folder = secrets.token_hex(10)
            main_path = os.path.join(self.results_path,self.folder)
            if(not os.path.exists(main_path)):
                break
        os.mkdir(main_path)
        self.path = main_path


    def prepare_files(self,X_train, X_test, Y_train, Y_test):
        X_train.index.name = None
        X_train.to_csv(os.path.join(self.path,"trainX.csv"))
        X_test.index.name = None
        X_test.to_csv(os.path.join(self.path,"testX.csv"))
        Y_train.index.name = None
        Y_train.to_csv(os.path.join(self.path,"trainY.csv"))
        Y_test.index.name = None
        Y_test.to_csv(os.path.join(self.path,"testY.csv"))

    def after_run(self):
        self.Y_train_hat = pd.read_csv(os.path.join(self.path,"trainYHat_{}.csv".format(self.id)),index_col=0)
        self.Y_test_hat = pd.read_csv(os.path.join(self.path,"testYHat_{}.csv".format(self.id)),index_col=0)
        self.Y_train_hat.columns=["t"]
        self.Y_test_hat.columns=["t"]

    def run(self):
        import subprocess
        args = ['java', '-jar', self.jar,
        "-id",str(self.id),
        "-i",self.path,
        "-o",self.path,
        "-e",str(self.epochs),
        "-seed",str(self.seed)
        ]
        s = subprocess.run(args,shell=True,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
        self.stdout = s.stdout.decode("utf-8")
        self.stderr = s.stderr.decode("utf-8")
        if (s.returncode == 0):
            self.after_run()
        

    def predict(self,X):
        if(X.shape[0] == self.Y_train_hat.shape[0]):
            return self.Y_train_hat.values
        elif(X.shape[0] == self.Y_test_hat.shape[0]):
            return self.Y_test_hat.values
        else:
            raise AttributeError("Must be same size")
        

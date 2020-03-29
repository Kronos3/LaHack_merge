from .models import *
import threading

l = threading.Lock()
index = 7956

query = Recipe.objects.all()
length = len(query)


def update_metadata(t_id):
    global index
    
    while index < length:
        l.acquire()
        c_index = index
        obj = query[c_index]
        
        index += 1
        l.release()
        
        try:
            obj.fill_metadata()
        except:
            print("Error %d/%d (%d)" % (c_index, length, t_id))
        else:
            print("Finished %d/%d (%d)" % (c_index, length, t_id))


def start_update():
    threads = []
    THREAD_NUM = 32
    
    for i in range(THREAD_NUM):
        threads.append(threading.Thread(target=update_metadata, args=(i,)))
    
    for t in threads:
        t.start()
    
    # Wait for everyone to finish
    for t in threads:
        t.join()

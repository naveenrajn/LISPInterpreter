/google/appengine/repackaged/com/google/common/base/Preconditions checkNotNull &(Ljava/lang/Object;)Ljava/lang/Object;
 lock 2
  preWriteCleanup 2
  getValueReference d()Lcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ValueReference; ! " � � $ unsetLiveEntry& �
 ' postWriteCleanup) 2
 * w x
 , � z
 . oldValue newValue 9(Ljava/lang/Object;ILjava/lang/Object;)Ljava/lang/Object; put :(Ljava/lang/Object;ILjava/lang/Object;Z)Ljava/lang/Object; expand5 2
 6  	 8 notifyValueReclaimed: 2 ; � O
 = newEntry �(Ljava/lang/Object;ILcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)Lcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;?@
 A set (ILjava/lang/Object;)VCD
 iE newCount index first onlyIfAbsent Z@    isCollected e(Lcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)ZMN
 O 	copyEntry((Lcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;Lcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)Lcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;QR
 S newIndex newNext tail 	tailIndex next 	headIndex head oldIndex oldTable oldCapacity newMask removeFromChain`R
 a newFirst ((Ljava/lang/Object;ILjava/lang/Object;)Z � � �e unsetKey enqueueNotification x(Ljava/lang/Object;ILcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ValueReference;)Vhi
 j enqueueCleanupl z
 m 
unsetValue x(Ljava/lang/Object;ILcom/google/appengine/repackaged/com/google/common/collect/CustomConcurrentHashMap$ValueReference;)Z v 
clearValue isUnsetsN
 t isComputingReferencev O w unsety!
 z offer| � �} �N
  � 2
 � processPendingCleanup 	cleanedUp incrementAndGet� o
 ;� isInlineCleanup� O
 � 
runCleanup� 2
 � isHeldByCurrentThread� O
 � cleanupExecutor Ljava/util/concurrent/Executor;��	 � java/util/concurrent/Executor� execute (Ljava/lang/Runnable;)V���� 
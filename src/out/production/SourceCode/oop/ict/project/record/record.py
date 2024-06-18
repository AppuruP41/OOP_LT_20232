from openai import OpenAI

api_key = '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'
output_file = 'Transcripts'
client = OpenAI(api_key=api_key)
audio_filename = 'C:\\Users\\Quang Hung\\Desktop\\OOP_LT_20232-release_01\\src\\oop\\ict\\project\\record\\recorded.wav'
output_file = 'transcripts.jsonl'
# Open the file
audio_file = open(audio_filename, 'rb')
response = client.audio.transcriptions.create (
    model="whisper-1",
    file=audio_file,
    response_format="text"
)

# Close the file
audio_file.close()

print (response)